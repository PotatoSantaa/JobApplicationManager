export class API {
    static loginUser(basicAuthToken) {
        return fetch(`${process.env.SPRING_APP_API_URL}/auth/basicauth`, {
            method: 'GET',
            headers: {
                'Authorization': `${basicAuthToken}`
            },
        }).then( resp => resp.json())
    }

    // static registerUser(body) {
    //     return fetch(`${process.env.REACT_APP_API_URL}/api/users/`, {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json',
    //         },
    //         body: JSON.stringify(body)
    //     }).then( resp => resp.json())
    // }
}