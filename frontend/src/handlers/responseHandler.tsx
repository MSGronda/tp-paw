import { parseLinkHeader } from "@web3-storage/parse-link-header";


export const handleResponse = (response: any) => {
    if(response){
        if(response.status >= 200 && response.status < 300){
            let parsed;
            parsed = parseLinkHeader(response.headers.link);
            if (!parsed) {
                return {
                    headers: response.headers,
                    status: response.status,
                    failure: false,
                    data: response.data
                }
            }
            let previousPage = "";
            let nextPage = "";
            if(parsed.prev)
                previousPage = parsed.prev.url;
            if(parsed.next)
                nextPage = parsed.next.url;
            return {
                headers: response.headers,
                status: response.status,
                failure: false,
                data: response.data,
                maxPage: parseInt(parsed.last.page),
                lastPage: parsed.last.url,
                firstPage: parsed.first.url,
                previousPage: previousPage,
                nextPage: nextPage

            }
        }
        return {
            status: response.status,
            failure: true
        };
    }
}