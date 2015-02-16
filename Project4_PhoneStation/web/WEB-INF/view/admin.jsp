<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page-header"><h1><fmt:message key="${pageCaption}"/></h1></div>

<c:if test="${data==null}">
    <h3><fmt:message key='entryAdminWelcome'/></h3>
</c:if>
    
<c:if test="${data=='actionResult'}">
    <h3><fmt:message key='${pageText}'/></h3>
</c:if>
    
<c:if test="${(data=='services')}">
    <table class="table table-hover">
        <thead>
            <tr>
                <th><fmt:message key="thName"/></th>
                <th><fmt:message key="thPrice"/></th>
                <th><fmt:message key="thDelete"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="serviceEntry" items="${servicesList}">
            <tr>
                <td>${serviceEntry.name}</td>
                <td>${serviceEntry.price}</td>
                <td>
                    <form action="admin" method="post">
                        <input type ="hidden" name="section" value="services"/>
                        <input type ="hidden" name="serviceId" value="${serviceEntry.id}"/>
                        <button class="btn btn-danger" type="submit" name="action" value="serviceDelete"><fmt:message key="btnDelete"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form action="admin" method="post">
    <input type ="hidden" name="section" value="services">
    <button class="btn btn-success" type="submit" name="action" value="serviceAddForm"><fmt:message key="btnAdd"/></button>
    </form>
</c:if>

<c:if test="${data=='serviceAddForm'}">
    <form action="admin" method="post">
        <fieldset>
            <div class="control-group">
                <label class="control-label" for="serviceName"><fmt:message key='lblServiceName'/></label>
                <div class="controls">
                    <input name="serviceName" placeholder="<fmt:message key='plhServiceName'/>" class="input-xlarge" type="text">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="cost"><fmt:message key='lblCost'/></label>
                <div class="controls">
                    <input name="cost" placeholder="<fmt:message key='plhCost'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-success" type="submit" name="action" value="serviceAddCompletion"><fmt:message key="btnAdd"/></button>
                </div>
            </div>
            
            <input type="hidden" name="section" value ="services">
        </fieldset>
    </form>
</c:if>  
    
<c:if test="${(data=='bills')}">
    <form action="admin" method="post">
        <fieldset>
        <div class="row">
            <div class="col-md-3">
                <label class="control-label" for="startDate"><fmt:message key='lblStartDate'/></label>
                <input value="${startDate}" name="startDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>
            <div class="col-md-3">
                <label class="control-label" for="endDate"><fmt:message key='lblEndDate'/></label>
                <input value="${endDate}" name="endDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>
            <div class="col-md-3">
                <label class="control-label" for="endDate"><fmt:message key='lblUser'/></label>
                <select name="selectedUserId">
                    <option value="-1">--</option>
                    <c:forEach var="userEntry" items="${usersList}">
                        <option value="${userEntry.id}" 
                                <c:if test="${selectedUserId==userEntry.id}">selected</c:if> 
                                >${userEntry.userName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <button class="btn btn-success" type="submit" name="action" value="${showAction}"><fmt:message key="btnUpdateView"/></button>
            </div>  
        </div>
        <div class="container">
        <table class="table table-hover">
        <thead>
            <tr>
                <th><fmt:message key="thDate"/></th>
                <th><fmt:message key="thDateIssued"/></th>
                <th><fmt:message key="thAmount"/></th>
                <th><fmt:message key="thPay"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="billEntry" items="${billsList}">
            <tr>
                <td>${billEntry.dateIssued}</td>
                <td>${billEntry.payMonth}</td>
                <td>${billEntry.amount}</td>
                <td>
                    <c:if test="${billEntry.isPayed}==true">
                    <input type="hidden" name ="billId" value="${billEntry.id}">
                    <button class="btn btn-success" type="submit" name="action" value="payBill"><fmt:message key="btnPayBill"/></button>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>    
        <input type="hidden" name="section" value ="bills">
        </fieldset>
    </form>
</c:if>

<c:if test="${(data=='callsListing')}">
    <form action="admin" method="post">
        <fieldset>
        <div class="row">
            <div class="col-md-3">
                <label class="control-label" for="startDate"><fmt:message key='lblStartDate'/></label>
                <input value="${startDate}" name="startDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>
            <div class="col-md-3">
                <label class="control-label" for="endDate"><fmt:message key='lblEndDate'/></label>
                <input value="${endDate}" name="endDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
            </div>
            <div class="col-md-3">
                <label class="control-label" for="endDate"><fmt:message key='lblUser'/></label>
                <select name="selectedUserId">
                    <option value="-1">--</option>
                    <c:forEach var="userEntry" items="${usersList}">
                        <option value="${userEntry.id}"
                                <c:if test="${selectedUserId==userEntry.id}">selected</c:if> 
                                >${userEntry.userName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <button class="btn btn-success" type="submit" name="action" value="showAll"><fmt:message key="btnUpdateView"/></button>
            </div>  
        </div>
        <div class="container">
        <table class="table table-hover">
        <thead>
            <tr>
                <th><fmt:message key="thDate"/></th>
                <th><fmt:message key="thUser"/></th>
                <th><fmt:message key="thDuration"/></th>
                <th><fmt:message key="thCost"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="callEntry" items="${callsList}">
            <tr>
                <td>${callEntry.timeStart}</td>
                <td>${callEntry.timeStart}</td>
                <td>${callEntry.duration}</td>
                <td>${callEntry.cost}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>    
        <input type="hidden" name="section" value ="calls">
        </fieldset>
    </form>
</c:if>
    
<c:if test="${(data=='usersListing')}">
    
        <fieldset>
        <div class="container">
        <table class="table table-hover">
        <thead>
            <tr>
                <th>Id</th>
                <th><fmt:message key="thUser"/></th>
                <th><fmt:message key="thPhone"/></th>
                <th><fmt:message key="thAdmin"/></th>
                <th><fmt:message key="thBlocked"/></th>
                <th><fmt:message key="thEdit"/></th>
                <th><fmt:message key="thDelete"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="userEntry" items="${usersList}">
            <tr>
                <td>${userEntry.id}</td>
                <td>${userEntry.userName}</td>
                <td>${userEntry.phoneNumber}</td>
                <td>${userEntry.isAdmin}</td>
                <td>${userEntry.isDisabled}</td>
                <td>
                    <form action="admin" method ="post">
                        <input type="hidden" name="selectedUserId" value="${userEntry.id}"/>
                        <input type="hidden" name="section" value ="abonents">
                        <button class="btn btn-primary" type="submit" name="action" value="editAbonent"><fmt:message key="btnEdit"/></button>
                    </form>
                </td>
                <td>
                    <form action="admin" method ="post">
                        <input type="hidden" name="selectedUserId" value="${userEntry.id}"/>
                        <input type="hidden" name="section" value ="abonents">
                        <button class="btn btn-danger" type="submit" name="action" value="deleteAbonent"><fmt:message key="btnDelete"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>
        <form action="admin" method="post">    
            <input type="hidden" name="section" value ="abonents">
            <button class="btn btn-success" type="submit" name="action" value="addAbonentForm"><fmt:message key="btnAdd"/></button>
        </form>
        </fieldset>
</c:if>
    
<c:if test="${data=='addAbonentForm'}">
    <form action="admin" method="post">
        <div class="col-lg-6">
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="username"><fmt:message key='lblLogin'/></label>
                    <input value="${user.userName}" name="username" placeholder="<fmt:message key='plhLogin'/>" class="input-xlarge" type="text">
                </div>  
            </div>
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="password"><fmt:message key='lblPassword'/></label>
                    <input name="password" placeholder="<fmt:message key='plhPassword'/>" class="input-xlarge" type="password">
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="phoneNumber"><fmt:message key='lblPhone'/></label>
                    <input value = "${user.phoneNumber}" name="phoneNumber" placeholder="<fmt:message key='plhPhone'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input name="isAdmin" class="input-xlarge" type="checkbox">
                    <label class="control-label" for="isAdmin"><fmt:message key='lblIsAdmin'/></label>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input name="isBlocked" class="input-xlarge" type="checkbox">
                    <label class="control-label" for="isBlocked"><fmt:message key='lblIsBlocked'/></label>
                </div>
            </div>
            <div class="control-group">
                
                <div class="controls">
                    <button class="btn btn-success" type="submit" name="action" value="addAbonentCompletion"><fmt:message key="btnAdd"/></button>
                </div>
            </div>
            
            <input type="hidden" name="section" value ="abonents">
        </div>
    </form>
</c:if>    


    
    
<c:if test="${(data=='callRegisterForm')}">
    <form action="abonent" method="post">
        <fieldset>
            <div class="control-group">
                <label class="control-label" for="registerDate"><fmt:message key='lblRegisterDate'/></label>
                <div class="controls">
                    <input value="${registerDate}" name="registerDate" placeholder="<fmt:message key='plhDate'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="duration"><fmt:message key='lblDuration'/></label>
                <div class="controls">
                    <input name="duration" placeholder="<fmt:message key='plhDuration'/>" class="input-xlarge" type="text">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="cost"><fmt:message key='lblCost'/></label>
                <div class="controls">
                    <input name="cost" placeholder="<fmt:message key='plhCost'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-success" type="submit" name="action" value="registerComplete"><fmt:message key="btnAdd"/></button>
                </div>
            </div>
            
            <input type="hidden" name="section" value ="calls">
        </fieldset>
    </form>
</c:if>