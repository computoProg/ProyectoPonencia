<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
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
            <title>Nuevo Participantes</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Nuevo Participantes</h1>
        <h:form>
            <h:inputHidden id="validateCreateField" validator="#{participantes.validateCreate}" value="value"/>
            <h:panelGrid columns="2">
                <h:outputText value="* Cedula:"  style ="font-weight:bold"/>
                <h:inputText id="cedula"
                             value="#{participantes.participantes.cedula}" 
                             title="Cedula" 
                             required="true" 
                             requiredMessage="La cédula es obligatoria." />
                
                <h:outputText value="* Nombre:" style ="font-weight:bold"/>
                <h:inputText id="nombre" 
                             value="#{participantes.participantes.nombre}" 
                             title="Nombre del participante" 
                             required="true" 
                             requiredMessage="El nombre del participante es obligatorio." />
                
                <h:outputText value="* Apellidos:" style="font-weight:bold"/>
                <h:inputText id="apellido"
                             value="#{participantes.participantes.apellido}"
                             title="Apellido"
                             required="true"
                             requiredMessage="El apellido es obligatorio." />
                
                <h:outputText value="* Email:" style ="font-weight:bold"/>
                <h:inputText id="email" 
                             value="#{participantes.participantes.email}" 
                             title="Email" 
                             required="true" 
                             requiredMessage="El E-mail es obligatorio." />
                
                
                <h:outputText value="* Vinculacion:" style ="font-weight:bold"/>
                <h:selectOneMenu id="vinculacion" 
                                 value="#{participantes.participantes.vinculacion}" 
                                 title="Vinculacion del participante" 
                                 required="true" 
                                 requiredMessage="La vinculación es obligatoria." >
                    <f:selectItems value="#{vinculacion.vinculacionItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                
                <h:outputText value="* Rol:" style ="font-weight:bold"/>
                <h:selectOneMenu id="rol" 
                                 value="#{participantes.participantes.rol}" 
                                 title="Rol del participante" 
                                 required="true"
                                 requiredMessage="El rol es obligatorio." >
                    <f:selectItems value="#{rol.rolItemsAvailableSelectOneAdmin}"/>
                </h:selectOneMenu>

                <h:outputText value="* Grupo Investigacion:"/>
                <h:selectOneMenu id="grupoinvestigacion" 
                                 value="#{participantes.participantes.grupoInvestigacionidGrupoInvestigacion}" 
                                 title="Grupo Investigacion"  
                                 required="true" 
                                 requiredMessage="El grupo de investigación es obligatorio." >
                    <f:selectItems value="#{GrupoInvestigacion.grupoInvestigacionItemsAvailableSelectOneActivos}"/>
                </h:selectOneMenu>
                
                <h:outputText value="* Contraseña:"style ="font-weight:bold"/>
                <h:inputSecret id="contraseña" 
                               value="#{participantes.participantes.contraseña}" 
                               title="Contraseña" 
                               required="true" 
                               requiredMessage="La contraseña es obligatoria." />
                
                <h:outputText value="* Confirmar Contraseña:" style ="font-weight:bold"/>
                <h:inputSecret id="contraseñav" 
                               value="#{participantes.participantes.contraseñav}"
                               title="Validación de la contraseña"
                               required="true"
                               requiredMessage="La validación de la contraseña es obligatoria." />

            </h:panelGrid>
            <br />
            <h:commandButton action="#{participantes.create}" value="Crear"/>
            <br />
            <br />
            <h:commandButton action="#{participantes.listSetup}" value="Mostrar participantes" immediate="true"/>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />

        </h:form>
        </body>
    </html>
</f:view>
