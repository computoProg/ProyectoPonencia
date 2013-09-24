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
                    </h:commandLink></div>
        </h:form>
            <title>Crear Ponencia</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" 
                        infoStyle="color: green" 
                        layout="table"/>
        </h:panelGroup>
            
            <h:outputText value="HOME > Consultar ponencia - Mis ponencias"/>
            
        <h1>Crear Ponencia</h1>
        <h:form>
            <h:inputHidden id="validateCreateField" validator="#{ponencia.validateCreate}" value="value"/>
            <h:panelGrid columns="2">
                <h:outputText  value="participante" rendered="#{participantes.p.rol.idrol != 2}" />
                <h:inputText id="pariticipante" maxlength="11" value="#{ponencia.ponencia.participantesCedula}" rendered="#{participantes.p.rol.idrol != 2}"  />
                
                <h:outputText value="Cédula:" style="font-weight: bold" rendered="#{participantes.p.rol.idrol == 2}" /> 
                <h:outputText value="#{participantes.participantes.cedula}" title="Cedula" rendered="#{participantes.p.rol.idrol == 2}" style="font-weight:bold"/>
                <%--<h:outputText value="#{ponencia.ponencia1(participantes.participantes.cedula).participantesCedula}" title="Cedula" rendered="#{participantes.p.rol.idrol == 2}" style="font-weight:bold"/>
                --%>
                <h:outputText value="* Fecha de la ponencia:" style="font-weight:bold"/>
                <rich:calendar id="Fecha"
                               value="#{ponencia.ponencia.fechaInicial}"
                               required="true"
                               requiredMessage="La fecha de la ponencia es obligatoria."/>
                               
                <h:outputText value="* Nombre:" style="font-weight:bold"/>
                <h:inputText id="nombre"
                             maxlength="45"
                             value="#{ponencia.ponencia.nombre}"
                             required="true"
                             requiredMessage="El nombre de la ponencia es obligatorio."/>
                
                <h:outputText value="* Ciudad" style="font-weight:bold"  />                    
                    <h:selectOneMenu id="ciudad" 
                                     value="#{ponencia.ponencia.ciudadidCiudad}"
                                     title="Ciudad de la ponencia"
                                     required="true"
                                     requiredMessage="La ciudad de la ponencia es obligatoria.">
                        <f:selectItems value="#{Ciudad.ciudadItemsAvailableSelectOne}"/>
                    </h:selectOneMenu>
                
                <h:outputText value="* Tipo:" style="font-weight:bold"/>
                <h:selectOneMenu id="tipo"
                                 value="#{ponencia.ponencia.tipoIdtipo}"
                                 title="Tipo de la ponencia"
                                 required="true"
                                 requiredMessage="El tipo de ponencia es obligatorio.">
                    <f:selectItems value="#{TipoPonencia.tipoItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                
                
                <h:outputText value="* Apoyo:" style="font-weight:bold"/>
                <h:selectOneMenu id="apoyo"
                                 value="#{ponencia.ponencia.apoyoIdapoyo}"
                                 title="Apoyo de la ponencia"
                                 required="true"
                                 requiredMessage="El apoyo de la ponencia es obligatorio.">
                    <f:selectItems value="#{apoyo.apoyoItemsAvailableSelectOneActivos}"/>
                </h:selectOneMenu>
                
                
                <h:outputText value="* Evento:" style="font-weight:bold"/>
                <h:inputText id="evento"
                             maxlength="45"
                             value="#{ponencia.ponencia.evento}"
                             required="true"
                             requiredMessage="El evento de la ponencia es obligatorio."/>
                
                <h:outputText value="* Proyecto:" style="font-weight:bold"/>
                <h:selectOneMenu id="proyecto"
                                 value="#{ponencia.ponencia.proyectoIdproyecto}"
                                 title="Proyecto al que esta asociada la ponencia"
                                 required="true"
                                 requiredMessage="El proyecto es obligatorio.">
                    <f:selectItems value="#{proyecto.proyectoItemsAvailableSelectOneActivos}"/>
                </h:selectOneMenu>
                
                <h:outputText value="Descripcion(600 caracteres):"/>                
                <h:inputTextarea id="descripcion" 
                                 label="Descripción de la ponencia"
                             value="#{ponencia.ponencia.descripcion}"
                             title="Descripcion de la ponencia (solo se permiten 600 caracteres)"
                             >
                    <f:validateLength maximum="600"/>
                </h:inputTextarea>
                
            </h:panelGrid>
            
            <br />
                <h:commandButton action="#{ponencia.create(participantes.participantes.cedula, participantes.p.rol.idrol)}" value="Guardar Ponencia ">
                <f:param name="jsfcrud.currentPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][ponencia.ponencia][ponencia.converter].jsfcrud_invoke}"/>
                </h:commandButton>
            <br />
            <br />
            <h:commandLink value="Volver a listar las Ponencias" action="#{ponencia.listSetup(participantes.p.rol.idrol, participantes.p.cedula)}" immediate="true" />
            </h:form>
                       
        </body>
    </html>
</f:view>
