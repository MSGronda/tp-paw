
//TODO cambiar constantes   
import {NavigateFunction} from "react-router/dist/lib/hooks";

export const handleService = (response: any, navigate: NavigateFunction, defaultValue= undefined) => {
    if (response){
        if( response.failure ){
            if (response.status === 204){
                return defaultValue
            } else if( response.status === 401 ){
                navigate('/login')
                return;
            } else if( response.status === 404){
                navigate('/error?code=404')
                return;
            }else{
                navigate('/error?code=500')
                return;
            }
        } else{
            return response.data
        }
    }
}

function getNextPage(response: any){
    if(response.current < response.maxPage){
        return response.current + 1;
    }
    return response.current;
}

export const handlePagedService = (response: any, navigate: NavigateFunction, defaultValue= undefined) => {
    if (response){
        if( response.failure ){
            if (response.status === 204){
                return defaultValue
            } else if( response.status === 401 ){
                navigate('/login')
                return;
            } else if( response.status === 404){
                navigate('/error?code=404')
                return;
            }else{
                navigate('/error?code=500')
                return;
            }
        } else{
            return [response.data, getNextPage(response)];
        }
    }
}