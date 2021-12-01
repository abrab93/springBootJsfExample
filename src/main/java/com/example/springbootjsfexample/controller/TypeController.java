package com.example.springbootjsfexample.controller;

import com.example.springbootjsfexample.config.Messages;
import com.example.springbootjsfexample.model.Type;
import com.example.springbootjsfexample.repository.TypeRepository;
import com.example.springbootjsfexample.service.TypeService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import com.example.springbootjsfexample.controller.util.JsfUtil;
import com.example.springbootjsfexample.controller.util.JsfUtil.PersistAction;

@Controller("typeController")
@SessionScope
public class TypeController implements Serializable {

    @Autowired
    private TypeRepository ejbFacade;
    @Autowired
    private Messages messages;
    private List<Type> items = null;
    private Type selected;

    public TypeController() {
    }

    public Type getSelected() {
        return selected;
    }

    public void setSelected(Type selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TypeRepository getFacade() {
        return ejbFacade;
    }

    public Type prepareCreate() {
        selected = new Type();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, messages.get("TypeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, messages.get("TypeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, messages.get("TypeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Type> getItems() {
        if (items == null) {
            items = IteratorUtils.toList(getFacade().findAll().iterator());
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().save(selected);
                } else {
                    getFacade().delete(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (Exception ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            }
//            (Exception ex) {
//                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
//                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
//            }
        }
    }

    public Type getType(Integer id) {
        return getFacade().findById(id).orElse(null);
    }

    public List<Type> getItemsAvailableSelectMany() {
        return IteratorUtils.toList(getFacade().findAll().iterator());
    }

    public List<Type> getItemsAvailableSelectOne() {
        return IteratorUtils.toList(getFacade().findAll().iterator());
    }

    @FacesConverter(forClass = Type.class)
    public static class TypeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TypeController controller = (TypeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "typeController");
            return controller.getType(getKey(value));
        }

        Integer getKey(String value) {
            Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Type) {
                Type o = (Type) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Type.class.getName()});
                return null;
            }
        }

    }

}
