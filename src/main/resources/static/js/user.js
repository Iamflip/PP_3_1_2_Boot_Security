const urlUsers = 'http://localhost:8081'

navbarAndUserInfoPage()

function navbarAndUserInfoPage(){
    const elementNavbar = document.getElementById('Navbar')
    const elementUserInfoPage = document.getElementById('userInfoPage')


    fetch(urlUsers + '/authUser')
        .then(response => response.json())
        .then(authUser => {
            console.log(authUser.username)
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