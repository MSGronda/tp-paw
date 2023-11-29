

export const handleResponse = (response: any) => {
    if(response){
        if(response.status >= 200 && response.status < 300){
            return {
                headers: response.headers,
                status: response.status,
                failure: false,
                data: response.data
            }
        }
        return {
            status: response.status,
            failure: true
        };
    }
}