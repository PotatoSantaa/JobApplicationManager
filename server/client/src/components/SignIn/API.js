export class API {
    static loginUser(body, token) {
        return fetch(`${process.env.REACT_APP_API_URL}/authenticate`, {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',        
            },
            body: JSON.stringify(body)
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