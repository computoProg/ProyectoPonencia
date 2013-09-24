<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%--
    This file is an entry point for JavaServer Faces application.
--%>
<f:view>
    <%/*
  HttpSession SesionUsuario = request.getSession(true);

response.setHeader( "Pragma", "no-cache" );
response.addHeader( "Cache-Control", "must-revalidate" );
response.addHeader( "Cache-Control", "no-cache" );
response.addHeader( "Cache-Control", "no-store" );
response.setDateHeader("Expires", 0);
// u=SesionUsuario.getAttribute(user);
if (request.getSession()==null){*/
%>

<%// } %>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <h:form>                
                <h:graphicImage value="../Home.png"/><h:outputText value="inicio"/>
                
                <h:commandLink  title="cambiar contraseña" action="#{participantes.go_change}">
                    <h:graphicImage value="../clave.png"/>
                    <Strong>
                        <h:outputText value="Cambiar contraseña"/>
                    </Strong> 
                </h:commandLink> | 
                <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                    <h:graphicImage value="../user.png"/>           
                    <Strong>
                        <h:outputText value="#{user.nombre}"/>
                    </Strong> 
                </h:commandLink> |
                <h:outputText value=" #{participantes.fecha}"/> 
                <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png"/>
                    </h:commandLink>
                </div>
        </h:form>
            <title>Ponencias</title>  

<link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
           <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            <h1><h:outputText value="HOME ADMINISTRADOR"/></h1>
            <h:form>
            <h:commandButton value="Consultar Ponencia" action="#{ponencia.listSetup(1,participantes.p.cedula)}"/>
            <h:commandButton value="Ingresar Ponencia" action="#{ponencia.createSetup}"/>
            </h:form>
            
            <h:form>
            <h:commandButton value="Participantes" action="#{participantes.listSetup}" 
                             rendered="#{participantes.p.rol.idrol == 1}" />
            </h:form>
            
            <h:form>
            <h:commandButton value="Proyectos" action="#{proyecto.listSetup}"/>
            </h:form>
            
            <h:form>
            <h:commandButton value="Grupos de Investigación" action="#{GrupoInvestigacion.listSetup}"/>
            </h:form>
            
            <h:form>
                <h:commandButton value="Apoyos" action="#{apoyo.listSetup}"/>
            </h:form>
            
            <h:form>
                <h:commandButton value="Pais, Ciudad" action="#{Ciudad.listSetup}"/>
            </h:form>
            
            <h:form>
            <h:commandButton action="#{participantes.detailSetup}" value="Gestionar Administrador"/>  
            </h:form>
            
            <br />
            <h:form>
                  <h:commandButton action="#{participantes.logout}" value="Salir"/>
            </h:form>
            
        </body>
    </html>
</f:view>
