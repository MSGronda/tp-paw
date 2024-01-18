
//TODO cambiar constantes   
export const handleService = (response: any, navigate: any, defaultValue= undefined) => {
    if (response){
        if( response.failure ){
            if (response.status === 204){
                return defaultValue
            } else if( response.status === 401 ){
                navigate('/login')
                return;
            } else if( response.status === 404){
                navigate('/404')
                return;
            }else{
                navigate('/500')
                return;
            }
        } else{
            return response.data
        }
    }
}