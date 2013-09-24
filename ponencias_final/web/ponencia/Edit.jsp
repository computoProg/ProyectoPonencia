<%@page contentType="text/html"%>
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
            <title>Editar Ponencia</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            
            <h:outputText value="HOME > Consultar ponencia - Editar ponencia"/>
            
            <h1>Editar Ponencia: <h:outputText value="#{ponencia.ponencia.nombre}"/></h1>
        <h:form>
            
            <h:panelGrid columns="2">
                <h:outputText value="ID:"/>
                <h:outputText value="#{ponencia.ponencia.idponencia}"/> 
                
                <h:outputText value="* Fecha de la ponencia:" style="font-weight:bold"/>
                <rich:calendar id="Fecha"
                               value="#{ponencia.ponencia.fechaInicial}"/>
               
                <%--
                <h:outputText value="* País:" style="font-weight:bold"/>
                <h:selectOneMenu id="pais"
                                 readonly="true"
                                 title="Pais en la que se realizó la ponencia"
                                 required="true"
                                 requiredMessage="El país es obligatorio">
                    <f:selectItems value="#{Pais.paisItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                
                <h:outputText value="* Departamento:" style="font-weight:bold"/>
                <h:selectOneMenu id="departamento"
                                 readonly="true"
                                 title="Departamento en el que se realizó la ponencia"
                                 required="true"
                                  requiredMessage="El departamento es obligatorio.">
                    <f:selectItems value="#{Departamento.departamentoItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                --%>
                
                <h:outputText value="* Ciudad:" style="font-weight:bold"/>
                <h:selectOneMenu id="ciudad"
                                 value="#{ponencia.ponencia.ciudadidCiudad}"
                                 title="Ciudad en la que se realizó la ponencia"
                                 required="true"
                                 requiredMessage="La Ciudad es obligatoria.">
                    <f:selectItems value="#{Ciudad.ciudadItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                
                <h:outputText value="* Nombre:" style="font-weight:bold"/>
                <h:inputText id="nombre"
                             maxlength="45"
                             value="#{ponencia.ponencia.nombre}"/>
                
                <h:outputText value="* Tipo:" style="font-weight:bold"/>
                <h:selectOneMenu id="tipo"
                                 value="#{ponencia.ponencia.tipoIdtipo}"
                                 title="Tipo de la ponencia"
                                 required="true"
                                 requiredMessage="El tipo de ponencia es obligatorio.">
                    <f:selectItems value="#{TipoPonencia.tipoItemsAvailableSelectOne}"/>
                </h:selectOneMenu>
                
                <h:outputText value="Apoyo:" style="font-weight:bold"
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.apoyoIdapoyo.estado)}"/>
                <h:outputText value="#{ponencia.ponencia.apoyoIdapoyo.apoyo}"
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.apoyoIdapoyo.estado)}"/>
                
                <h:outputText value="Leer mensaje si requiere editar la entidad de apoyo:" style="color:red; font-weight:bold" 
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.apoyoIdapoyo.estado)}"/>
                <h:outputText value="La ponencia tiene la entidad de apoyo deshabilitada actualmente.  Favor de comunicarse con el Centro de Investigación de la Facultad, para que lo apoye con la edición de la entidad de apoyo de esta Ponencia."
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.apoyoIdapoyo.estado)}"/>
                
                <h:outputText value="* Apoyo:" style="font-weight:bold"
                              rendered="#{apoyo.validaEstado(ponencia.ponencia.apoyoIdapoyo.estado)}"/>
                <h:selectOneMenu id="apoyo"
                                 value="#{ponencia.ponencia.apoyoIdapoyo}"
                                 title="Apoyo de la ponencia"
                                 required="true"
                                 requiredMessage="El apoyo de la ponencia es obligatorio."
                                 rendered="#{apoyo.validaEstado(ponencia.ponencia.apoyoIdapoyo.estado)}">
                    <f:selectItems value="#{apoyo.apoyoItemsAvailableSelectOneActivos}"/>
                </h:selectOneMenu>
                
                <h:outputText value="* Evento:" style="font-weight:bold"/>
                <h:inputText id="evento"
                             maxlength="45"
                             value="#{ponencia.ponencia.evento}"/>
                
                <h:outputText value="Proyecto:" style="font-weight:bold" 
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.proyectoIdproyecto.estado)}"/>
                <h:outputText value="#{ponencia.ponencia.proyectoIdproyecto.nombre}"
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.proyectoIdproyecto.estado)}"/>
                
                <h:outputText value="Leer mensaje si requiere editar el proyecto:" style="color:red; font-weight:bold"
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.proyectoIdproyecto.estado)}"/>
                <h:outputText value="La ponencia tiene el proyecto deshabilitado actualmente.  Favor de comunicarse con el Centro de Investigación de la Facultad, para que lo apoye con la edición del proyecto de esta Ponencia."
                              rendered="#{!apoyo.validaEstado(ponencia.ponencia.proyectoIdproyecto.estado)}"/>                
                
                <h:outputText value="* Proyecto:" style="font-weight:bold"
                              rendered="#{apoyo.validaEstado(ponencia.ponencia.proyectoIdproyecto.estado)}"/>
                <h:selectOneMenu id="proyecto"
                                 value="#{ponencia.ponencia.proyectoIdproyecto}"
                                 title="Proyecto al que esta asociada la ponencia"
                                 required="true"
                                 requiredMessage="El proyecto es obligatorio."
                                 rendered="#{apoyo.validaEstado(ponencia.ponencia.proyectoIdproyecto.estado)}">                    
                    <f:selectItems value="#{proyecto.proyectoItemsAvailableSelectOneActivos}"/>
                </h:selectOneMenu>
                
                
                <%--<h:outputText value="Descripcion:"/>--%>
                <h:outputText value="Descripcion(600 caracteres):"/> 
                <h:inputTextarea id="descripcion"
                                 label="Descripción de la ponencia"
                             value="#{ponencia.ponencia.descripcion}"
                             title="Descripcion de la ponencia (solo se permiten 600 caracteres)">
                    <f:validateLength maximum="600"/>
                </h:inputTextarea>
                
            </h:panelGrid>
            
            
            <br />
            <h:commandButton action="#{ponencia.edit}" value="Guardar Ponencia">
                <f:param name="jsfcrud.currentPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][ponencia.ponencia][ponencia.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            
            <br />
            <br />
            <h:commandLink value="Volver a listar las Ponencias" action="#{ponencia.listSetup(participantes.p.rol.idrol, participantes.p.cedula)}" immediate="true" />
        </h:form>
            
        </body>
    </html>
</f:view>
