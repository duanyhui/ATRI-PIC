import axios from "axios";

export function HasPermission(satoken){
    return axios({
        url: '/utils/hasPermission',
        method: 'GET',
        params:{
          satoken: satoken
        }
    })
}
