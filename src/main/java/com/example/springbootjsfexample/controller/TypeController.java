package com.example.springbootjsfexample.controller;

import com.example.springbootjsfexample.config.Messages;
import com.example.springbootjsfexample.controller.util.JsfUtil;
import com.example.springbootjsfexample.controller.util.JsfUtil.PersistAction;
import com.example.springbootjsfexample.dto.TypeDto;
import com.example.springbootjsfexample.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller("typeController")
@SessionScope
@Slf4j
@RequiredArgsConstructor
public class TypeController implements Serializable {

    private final TypeService typeService;
    private final Messages messages;

    private List<TypeDto> items = null;
    private TypeDto selected;

    public TypeDto getSelected() {
        return selected;
    }

    public void setSelected(TypeDto selected) {
        this.selected = selected;
    }


    public TypeDto prepareCreate() {
        selected = new TypeDto();
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

    public List<TypeDto> getItems() {
        if (items == null) {
            items = typeService.findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    typeService.createOrUpdate(selected);
                } else {
                    typeService.delete(selected);
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

    public TypeDto getType(Integer id) {
        return typeService.findById(id);
    }

    public List<TypeDto> getItemsAvailableSelectMany() {
        return typeService.findAll();
    }

    public List<TypeDto> getItemsAvailableSelectOne() {
        return typeService.findAll();
    }

    @FacesConverter(forClass = TypeDto.class)
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
            if (object instanceof TypeDto) {
                TypeDto o = (TypeDto) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TypeDto.class.getName()});
                return null;
            }
        }

    }

}
