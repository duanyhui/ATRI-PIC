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
export function getPicByTag(tag, num){
    return axios({
        url: '/pic/getByTag',
        method: 'GET',
        params:{
            tag: tag,
            num: num
        }
    })
}

export function getAboutMe(){
    return axios({
        url: '/utils/about',
        method: 'GET'
    })
}
export function upload(data){
  //'Content-Type': 'multipart/form-data'

    return axios({
        url: '/pic/upload',
      Headers:{
        'Content-Type': 'multipart/form-data'
      },
        method: 'POST',
        data: data
    })
}
