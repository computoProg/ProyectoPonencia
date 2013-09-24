<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>

<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
             <h:form>
                <h:commandLink title="volver al inicio" action="#{participantes.validaWelcome}">
                    <h:graphicImage value="../Home.png"/><h:outputText value="inicio"/>
                </h:commandLink> | <h:commandLink  title="cambiar contraseÃ±a" action="#{participantes.go_change}">
                <h:graphicImage value="../clave.png"/>           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                <h:graphicImage value="../user.png"/>           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png"/>
                    </h:commandLink></div></h:form>
            <title>Nueva ciudad</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            <h:outputText value="HOME > Gestionar Proyectos"/>
        <h1>Crear Ciudad</h1>
        <%--<h:form>
            <h:inputHidden id="validateCreateField" 
                            validator="#{Pais.validateCreate}"
                            value="value"/>
             
             <h:commandButton value="Crear Pais" 
                              action="#{Pais.changeBoolPais}"/>
             
             <h:panelGroup rendered="#{Pais.boolPais}">
                    <h:panelGrid columns="2">
                        <h:outputText value="Nombre:" rendered="#{Pais.boolPais}"/>
                        <h:inputText value="#{Pais.pais.nombre}" 
                                     required ="true"
                                     requiredMessage="El nombre del País es obligatorio."/>
                        <h:outputText value=""/>
                        <h:commandButton value="Crear" 
                                         action="#{Pais.create}"/>
                    </h:panelGrid>
             </h:panelGroup>
        </h:form>
        --%>
        
        <%--<h:form>
             <h:commandButton value="Crear Departamento" 
                              action="#{Departamento.changeBoolDepto}"/>
             
             <h:panelGroup rendered="#{Departamento.boolDepto}">
                    <h:panelGrid columns="2">
                        <h:outputText value="Pais:"/>
                        <h:selectOneMenu value="#{Departamento.departamento.paisidPais}"
                                         title="Pais al que corresponde el departamento"
                                         required="true"
                                         requiredMessage="El país es obligatorio">
                            <f:selectItems value="#{Pais.paisItemsAvailableSelectOne}"/>
                        </h:selectOneMenu>
                        
                        <h:outputText value="Nombre:"/> 
                        <h:inputText value="#{Departamento.departamento.nombre}"
                                     title="Nombre del Departamento"
                                     required="true"
                                     requiredMessage="El nombre del departamento es obligatorio."/>
                        <h:outputText value=""/>
                        <h:commandButton value="Crear" action="#{Departamento.create}"/>
                    </h:panelGrid>
             </h:panelGroup>
             
        </h:form>
        --%>
             
        <h:form>
             <h:panelGrid columns="2">
                 <%--<h:outputText value="Pais:"
                               style="font-weight:bold"/>
                 <h:selectOneMenu readonly="true">
                     <f:selectItems value="#{Pais.paisItemsAvailableSelectOne}"/>
                 </h:selectOneMenu>
                 
                 <h:outputText value="Departamento:"
                              style="font-weight:bold"/>
                 <h:selectOneMenu value="#{Ciudad.ciudad.departamentoidDepartamento}"
                                  title="Departamento al que pertenece la nueva Ciudad."
                                  required="true"
                                  requiredMessage="El departamento es obligatorio">
                     <f:selectItems value="#{Departamento.departamentoItemsAvailableSelectOne}"/>
                 </h:selectOneMenu>
                 --%>
                 
                 <h:outputText value="Ciudad:"
                               style="font-weight:bold"/>
                 <h:inputText maxlength="45" value="#{Ciudad.ciudad.nombre}" 
                              required="true"
                              requiredMessage="El nombre de la ciudad es obligatorio"/>
                 <h:outputText value=""/>
                 <h:commandButton value="Crear" 
                             title="Crear ciudad"
                             action="#{Ciudad.create}"/>
             </h:panelGrid> 
             <%------------------------------------------%>
                <%--<h:panelGrid columns="3">     
                <h:outputText value="País:" style="font-weight:bold"/>
                <h:selectOneMenu id="pais"
                                 value="#{Departamento.departamento.paisidPais}">
                    <f:selectItems value="#{Pais.paisItemsAvailableSelectOne}"/>
                </h:selectOneMenu> 
                <h:commandButton value="Crear" 
                                 title="Crear país"
                                 rendered="#{!Pais.boolPais}"
                                 action="#{Pais.changeBoolPais}"/>
                
                <h:panelGroup rendered="#{Pais.boolPais}">
                    <h:panelGrid columns="2">
                        <h:outputText value="Nombre:" rendered="#{Pais.boolPais}"/>
                        <h:inputText value="#{Pais.pais.nombre}" 
                                     required ="true"
                                     requiredMessage="El nombre del País es obligatorio."/>
                        <h:commandButton value="Crear" 
                                         action="#{Pais.create}"/>
                    </h:panelGrid>
                </h:panelGroup>

                <h:outputText value="Departamento:" style="font-weight:bold"/>
                <h:selectOneMenu value = "#{Ciudad.ciudad.departamentoidDepartamento}">
                    <f:selectItems value="#{Departamento.departamentoItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                <h:commandButton value="Crear" 
                                 title="Crear Departamento"
                                 rendered="#{!Departamento.boolDepto}"
                                 action="#{Departamento.changeBoolDepto}"/>
                
                <h:panelGroup rendered="#{Departamento.boolDepto}">
                     
                    <h:panelGrid columns="2">
                        <h:outputText value="Nombre:"/> 
                        <h:inputText value="#{Departamento.departamento.nombre}"
                                     title="Nombre del Departamento"
                                     required="true"
                                     requiredMessage="El nombre del departamento es obligatorio."/>
                        <h:commandButton value="Crear" action="#{Departamento.create}"/>
                    </h:panelGrid>
                </h:panelGroup>
                <h:commandButton action="#{Departamento.changeBoolDepto}" value="Ocultar"/>
            <h:outputText value="Ciudad:" style="font-weight:bold"/>
            <h:inputText value="#{Ciudad.ciudad.nombre}" 
                         required = "true" 
                         requiredMessage="El nombre de la ciudad es obligatorio"/>
            <h:commandButton value="Crear" 
                             title="Crear ciudad"
                             action="#{Ciudad.create}"/>
            </h:panelGrid>--%>
                
        </h:form>
       
        </body>
    </html>
</f:view>
