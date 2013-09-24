<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
             <h:form>
                 <h:commandLink title="volver al inicio" value="Volver al inicio" action="login">
             </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> 
            </h:form>
            <title>No Activo</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
            <h1>Usted no se encuentra ACTIVO en el sistema</h1>
            Por favor dirijase al Administrador del sistema para activar de nuevo la cuenta
        </body>
</html>
</f:view>