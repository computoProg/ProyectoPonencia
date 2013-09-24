<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich" %>

<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
     <h:form>
         <h:commandLink title="volver al inicio" value="Volver al inicio" action="login">                    
                </h:commandLink>   |<h:outputText value=" #{participantes.fecha}"/> 
     </h:form>
            <title>Crear Cuenta</title><div align="center"/>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Crear Cuenta</h1>

        <h:form id="error">
            <h:panelGrid columns="2">
                <h:outputText value="* Cédula:" style="font-weight:bold"/>
                <h:inputText id="cedula"
                             maxlength="11"
                             value="#{participantes.participantes.cedula}"
                             title="Cedula" 
                             required="true"
                             requiredMessage="La cédula es obligatoria." />

                <h:outputText value="* Nombres:" style="font-weight:bold"/>
                <h:inputText id="nombre" 
                             maxlength="45"
                             value="#{participantes.participantes.nombre}"
                             title="Nombre" 
                             required="true"
                             requiredMessage="El nombre es obligatorio." />

                <h:outputText value="* Apellidos:" style="font-weight:bold"/>
                <h:inputText id="apellido"
                             maxlength="45"
                             value="#{participantes.participantes.apellido}"
                             title="Apellido"
                             required="true"
                             requiredMessage="El apellido es obligatorio." />


                <h:outputText value="* E-mail:" style="font-weight:bold"/>
                <h:inputText id="email" 
                             maxlength="60"
                             value="#{participantes.participantes.email}"
                             title="Email" required="true"
                             requiredMessage="El email es obligatorio." />

                <h:outputText value="* Vinculacion:" style="font-weight:bold"/>
                <h:selectOneMenu id="vinculacion"
                                 value="#{participantes.participantes.vinculacion}"
                                 title="Vinculacion"
                                 required="true"
                                 requiredMessage="El campo vinculación es obligatorio." >
                    <f:selectItems value="#{vinculacion.vinculacionItemsAvailableSelectOne}"/>
                </h:selectOneMenu>

               

                <h:outputText value="* Grupo Investigacion:" style="font-weight:bold"/>
                <h:selectOneMenu id="grupoinvestigacion"
                                 value="#{participantes.participantes.grupoInvestigacionidGrupoInvestigacion}"
                                 title="Grupo de Investigacion"
                                 required="true"
                                 requiredMessage="El  Grupo de Investigación es obligatorio" >
                    <f:selectItems value="#{GrupoInvestigacion.grupoInvestigacionItemsAvailableSelectOneActivos}"/>
                </h:selectOneMenu>


                <h:outputText value="* Contraseña:" style="font-weight:bold"/>
                <h:inputSecret id="contraseña" 
                               maxlength="45"
                               value="#{participantes.participantes.contraseña}"
                               title="Contraseña" 
                               required="true" 
                               requiredMessage="La contraseña es obligatoria." />
                <h:outputText value="* Confirmar Contraseña:" style="font-weight:bold"/>
                <h:inputSecret id="contraseñav" 
                               maxlength="45"
                               value="#{participantes.participantes.contraseñav}"
                               title="Ingrese de nuevo la contraseña" 
                               required="true" 
                               requiredMessage="La validación de la contraseña es obligatoria." />
            </h:panelGrid>
            <br />
            <h:commandButton action="#{participantes.create}" 
                             value="Crear Cuenta"/>
            <br />
           
            <br />
            <h:commandLink value="Volver al inicio" 
                           action="login" 
                           immediate="true" />

        </h:form>
        </body>
    </html>
</f:view>
