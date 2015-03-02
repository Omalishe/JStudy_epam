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
                <th><fmt:message key="thEdit"/></th>
                <th><fmt:message key="thDelete"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="serviceEntry" items="${servicesList}">
            <tr>
                <td><ch:displayNameSelector lang="${language}" obj="${serviceEntry}"/></td>
                <td>${serviceEntry.price}</td>
                <td>
                    <form action="admin" method="post">
                        <input type ="hidden" name="section" value="services"/>
                        <input type ="hidden" name="serviceId" value="${serviceEntry.id}"/>
                        <button class="btn btn-primary" type="submit" name="action" value="serviceEditForm"><fmt:message key="btnEdit"/></button>
                    </form>
                </td>
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
    <form action="admin" method="post" accept-charset="utf-8">
        <fieldset>
            <c:forEach var="langEntry" items="${languages}">
                <div class="control-group">
                    <label class="control-label" for="name_${langEntry}"><fmt:message key='lblServiceName'/> (${langEntry})</label>
                    <div class="controls">
                        <input name="name_${langEntry}" placeholder="<fmt:message key='plhServiceName'/>" class="input-xlarge" type="text">
                    </div>
                </div>
            </c:forEach>

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
    
<c:if test="${data=='serviceEditForm'}">
    <form action="admin" method="post" accept-charset="utf-8">
        <fieldset>
            <c:forEach var="langEntry" items="${languages}">
                <div class="control-group">
                    <label class="control-label" for="name_${langEntry}"><fmt:message key='lblServiceName'/> (${langEntry})</label>
                    <div class="controls">
                        <input value="<ch:displayNameSelector lang="${langEntry}" obj="${editedService}"/>" name="name_${langEntry}" placeholder="<fmt:message key='plhServiceName'/>" class="input-xlarge" type="text">
                    </div>
                </div>
            </c:forEach>

            <div class="control-group">
                <label class="control-label" for="cost"><fmt:message key='lblCost'/></label>
                <div class="controls">
                    <input value="${editedService.price}" name="cost" placeholder="<fmt:message key='plhCost'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-success" type="submit" name="action" value="serviceEditCompletion"><fmt:message key="btnEdit"/></button>
                </div>
            </div>
            <input type="hidden" name="serviceId" value="${editedService.id}"/>
            <input type="hidden" name="section" value ="services"/>
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
                                ><ch:displayNameSelector lang="${language}" obj="${userEntry}"/></option>
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
                <th><fmt:message key="thUser"/></th>
                <th><fmt:message key="thDateIssued"/></th>
                <th><fmt:message key="thAmount"/></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="billEntry" items="${billsList}">
            <tr>
                <td>${billEntry.dateIssued}</td>
                <td>${billEntry.userName}</td>
                <td>${billEntry.payMonth}</td>
                <td>${billEntry.amount}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
        </div>    
        <input type="hidden" name="section" value ="bills">
        <button class="btn btn-success" type="submit" name="action" value="createBill"><fmt:message key="btnAdd"/></button>
        
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
                                ><ch:displayNameSelector lang="${language}" obj="${userEntry}"/></option>
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
                <td>${callEntry.userName}</td>
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
                <th><fmt:message key="thGivenName"/></th>
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
                <td><ch:displayNameSelector lang="${language}" obj="${userEntry}"/></td>
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
    
<c:if test="${data=='editAbonentForm'}">
    <form action="admin" method="post" accept-charset="urf-8">
        <div class="col-lg-6">
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="username"><fmt:message key='lblLogin'/></label>
                    <input value="${editedUser.userName}" name="username" placeholder="<fmt:message key='plhLogin'/>" class="input-xlarge" type="text">
                </div>  
            </div>
                
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="password"><fmt:message key='lblPassword'/></label>
                    <input name="password" placeholder="<fmt:message key='plhPassword'/>" class="input-xlarge" type="password">
                </div>
            </div>
            
            <c:forEach var="langEntry" items="${languages}">
                <div class="control-group">
                    <div class="controls">
                    <label class="control-label" for="givenName_${langEntry}"><fmt:message key='lblGivenName'/> (${langEntry}) </label>
                        <input value="<ch:displayNameSelector lang="${langEntry}" obj="${editedUser}"/>" name="givenName_${langEntry}" placeholder="<fmt:message key='plhGivenName'/>" class="input-xlarge" type="text">
                    </div>  
                </div>
            </c:forEach>

            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="phoneNumber"><fmt:message key='lblPhone'/></label>
                    <input value="${editedUser.phoneNumber}" name="phoneNumber" placeholder="<fmt:message key='plhPhone'/>" class="input-xlarge" type="text">
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input name="isAdmin" class="input-xlarge" type="checkbox" <c:if test="${editedUser.isAdmin}">checked="true"</c:if> >
                    <label class="control-label" for="isAdmin"><fmt:message key='lblIsAdmin'/></label>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input name="isBlocked" class="input-xlarge" type="checkbox" <c:if test="${editedUser.isDisabled}">checked="true"</c:if> >
                    <label class="control-label" for="isBlocked"><fmt:message key='lblIsBlocked'/></label>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-success" type="submit" name="action" value="editAbonentCompletion"><fmt:message key="btnEdit"/></button>
                </div>
            </div>
            
            <input type="hidden" name="selectedUserId" value ="${editedUser.id}">
            <input type="hidden" name="section" value ="abonents">
        </div>
    </form>
</c:if>    

<c:if test="${data=='addAbonentForm'}">
    <form action="admin" method="post" accept-charset="utf-8">
        <div class="col-lg-6">
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="username"><fmt:message key='lblLogin'/></label>
                    <input name="username" placeholder="<fmt:message key='plhLogin'/>" class="input-xlarge" type="text">
                </div>  
            </div>
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="password"><fmt:message key='lblPassword'/></label>
                    <input name="password" placeholder="<fmt:message key='plhPassword'/>" class="input-xlarge" type="password">
                </div>
            </div>
            
            <c:forEach var="langEntry" items="${languages}">
                <div class="control-group">
                    <div class="controls">
                    <label class="control-label" for="givenName_${langEntry}"><fmt:message key='lblGivenName'/> (${langEntry}) </label>
                        <input name="givenName_${langEntry}" placeholder="<fmt:message key='plhGivenName'/>" class="input-xlarge" type="text">
                    </div>  
                </div>
            </c:forEach>
                
            <div class="control-group">
                <div class="controls">
                <label class="control-label" for="phoneNumber"><fmt:message key='lblPhone'/></label>
                    <input name="phoneNumber" placeholder="<fmt:message key='plhPhone'/>" class="input-xlarge" type="text">
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
