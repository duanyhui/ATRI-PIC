import axios from 'axios'

export function getRandPic(num){
    return axios({
        url: '/pic/rand',
        method: 'GET',
        params:{
            num: num
        }
    })
}

export function getPic(pid){
    return axios({
        url: '/pic/{pid}',
        method: 'GET'
    })
}
