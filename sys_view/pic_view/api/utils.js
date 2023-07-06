import axios from "axios";
export function SetUrlLog(url){
  return axios({
    url: '/utils/setUrlLog',
    method: 'POST',
    params:{
      url: url
    }
  })
}

export function GetHomeAbout(){
  return axios({
    url: '/utils/HomeAbout',
    method: 'GET'
  })
}

