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
        url: '/pic/get',
        method: 'GET',
      params:{
        pid: pid
      }
    })
}

export function votePic(pid, vote){
    return axios({
        url: '/pic/vote',
        method: 'POST',
        params:{
            pid: pid,
            vote: vote
        }
    })
}
