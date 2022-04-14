const urlUsers = 'http://localhost:8081/admin'
const urlRoles = 'http://localhost:8081/roles'
const urlAuthUser = 'http://localhost:8081/authUser'

getUsers()
navbarAndUserInfoPage()
roleAdd()

async function roleAdd() {
    let listRolesToAdd = ''
    await fetch(urlRoles)
        .then(response => response.json())
        .then(roles => roles.forEach(role => {
            listRolesToAdd += `
                        <option value="${role.name}"">${role.name.replace("ROLE_", "")}</option>`
        }))
    document.getElementById('roleAdd').innerHTML = listRolesToAdd
}

$(document).ready(function() {
    $('#editUser').on('hidden.bs.modal', function() {
        $(':input', this).val('');
    });
    $('#deleteUser').on('hidden.bs.modal', function() {
        $(':input', this).val('');
    });
});

async function getUsers() {
    const elementUsersTBody = document.getElementById('usersTBody')
    let listUsers = ''

    await fetch(urlUsers)
        .then(response => response.json())
        .then(users => users.forEach(user => {
            console.log(user)
            listUsers += `
                <tr id="rowUser${user.id}">
                    <td id="rowIdUser${user.id}">${user.id}</td>
                    <td id="rowFirstNameUser${user.id}">${user.firstName}</td>
                    <td id="rowLastNameUser${user.id}">${user.lastName}</td>
                    <td id="rowAgeUser${user.id}">${user.age}</td>
                    <td id="rowUsernameUser${user.id}">${user.username}</td>
                    <td id="rowRolesUser${user.id}">${user.roles.map(r => " " + r.name).map(name => name.replace("ROLE_", ""))}</td>
                    <td><a class="btn btn-info" data-toggle="modal" id="${user.id}" onclick="fillEditModalField(${user.id})" href="#editUser">Edit</a></td>
                    <td><a class="btn btn-danger" data-toggle="modal" id="${user.id}" onclick="fillDeleteModalField(${user.id})" href="#deleteUser">Delete</a></td>
                </tr>`
        }))
    elementUsersTBody.innerHTML = listUsers
}

function navbarAndUserInfoPage(){
    const elementNavbar = document.getElementById('Navbar')
    const elementUserInfoPage = document.getElementById('userInfoPage')

    fetch(urlAuthUser)
        .then(response => response.json())
        .then(authUser => {
            elementNavbar.textContent = `${authUser.username} with roles: ${authUser.authorities.map(a => a.name).map(name => name.replace("ROLE_", ""))}`
                elementUserInfoPage.innerHTML = `
                    <tr>
                    <td>${authUser.id}</td>
                    <td>${authUser.firstName}</td>
                    <td>${authUser.lastName}</td>
                    <td>${authUser.age}</td>
                    <td>${authUser.username}</td>
                    <td>${authUser.roles.map(r => " " + r.name).map(name => name.replace("ROLE_", ""))}</td>
                    </tr>`
            })
}

async function addUser(){

    console.log('add user')
    let user =
        {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            age: document.getElementById('age').value,
            username: document.getElementById('username').value,
            password: document.getElementById('password').value,
            roles: []
        }

        console.log(user)

    let stringRoles = document.getElementById('roleAdd').getElementsByTagName('option')
    for (let i = 0; i < stringRoles.length; i++) {
        if (stringRoles[i].selected){
            await fetch(urlRoles + '/' + stringRoles[i].value)
                .then(response => response.json())
                .then(role => user.roles.push(role))
        }
    }

    await fetch(urlUsers, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });
    getUsers()
    $('#usersTableButton').click();
}

async function editUser(id){
    console.log('edit user')
    let user =
        {
            id: document.getElementById('idEdit').value,
            firstName: document.getElementById('firstNameEdit').value,
            lastName: document.getElementById('lastNameEdit').value,
            age: document.getElementById('ageEdit').value,
            username: document.getElementById('usernameEdit').value,
            password: document.getElementById('passwordEdit').value,
            roles: []
        }

    let stringRoles = document.getElementById('roleEdit').getElementsByTagName('option')
    for (let i = 0; i < stringRoles.length; i++) {
        if (stringRoles[i].selected){
            await fetch(urlRoles + '/' + stringRoles[i].value)
                .then(response => response.json())
                .then(role => user.roles.push(role))
        }
    }

    await fetch(urlUsers, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });
    $('#editUser').modal('hide');

    await fetch(urlUsers + '/' + id)
        .then(response => response.json())
        .then(user => {
            console.log(user.age)
            document.getElementById('rowIdUser' + id).textContent = user.id
            document.getElementById('rowFirstNameUser' + id).textContent = user.firstName
            document.getElementById('rowLastNameUser' + id).textContent = user.lastName
            document.getElementById('rowAgeUser' + id).textContent = user.age
            document.getElementById('rowUsernameUser' + id).textContent = user.username
            document.getElementById('rowRolesUser' + id).textContent = user.roles.map(r => " " + r.name).map(name => name.replace("ROLE_", ""))
        })
}

async function deleteUser(id) {
    console.log('delete user')
    await fetch(urlUsers + '/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(id)
    });
    $('#deleteUser').modal('hide');
    await document.getElementById('rowUser' + id).remove()
}

function fillEditModalField(id) {
    let listRoles = ''

    fetch(urlUsers + '/' + id)
        .then(response => response.json())
        .then(async user => {
            document.getElementById('idEdit').value = user.id
            document.getElementById('firstNameEdit').value = user.firstName
            document.getElementById('lastNameEdit').value = user.lastName
            document.getElementById('ageEdit').value = user.age
            document.getElementById('usernameEdit').value = user.username
            await fetch(urlRoles)
                .then(response => response.json())
                .then(roles => roles.forEach(role => {
                    listRoles += `
                        <option value="${role.name}">${role.name.replace("ROLE_", "")}</option>`
                }))
            document.getElementById('roleEdit').innerHTML = listRoles
            document.getElementById('modalEditButton').addEventListener('click', () => editUser(user.id))
        })
}

function fillDeleteModalField(id) {
    fetch(urlUsers + '/' + id)
        .then(response => response.json())
        .then(user => {
            document.getElementById('idDelete').value = user.id
            document.getElementById('firstNameDelete').value = user.firstName
            document.getElementById('lastNameDelete').value = user.lastName
            document.getElementById('ageDelete').value = user.age
            document.getElementById('usernameDelete').value = user.username
            document.getElementById('modalDeleteButton').addEventListener('click', () => deleteUser(user.id))
        })
}