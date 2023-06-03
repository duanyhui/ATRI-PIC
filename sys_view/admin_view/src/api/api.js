import axios from 'axios'
export function Login(username, password) {
    return axios({
        url: '/admin/login',
        method: 'POST',
        params: {
            username: username,
            password: password
        }
    })
}
export function GetCheckPicList() {
    return axios({
        url: '/picCheck/getCheckPicList',
        method: 'GET',

    })
}

export function DeletePic(pids) {
    return axios({
        url: '/picCheck/delete',
        method: 'POST',
        //数组类型
        data: pids
    })
}
export function AcceptPic(pids) {
    return axios({
        url: '/picCheck/accept',
        method: 'POST',
        //数组类型
        data: pids
    })
}

export function AcceptAllPic(){
    return axios({
        url: '/picCheck/acceptAll',
        method: 'POST',
    })
}

export function DeleteAllUnCheckPic() {
    return axios({
        url: '/picCheck/deleteAll',
        method: 'POST',
    })
}

export function GetPicList() {
    return axios({
        url: '/picCheck/getAllPicList',
        method: 'GET',
    })
}

export function ForbidPic(pids) {
    return axios({
        url: '/picCheck/forbid',
        method: 'POST',
        //数组类型
        data: pids
    })
}
export function GetUserInfoList(){
    return axios({
        url: '/admin/getUserInfoList',
        method: 'GET',
    })
}
export function GetLogList(){
    return axios({
        url: '/admin/getLogList',
        method: 'GET',
    })
}

export function AddPicTag(pid,tag){
    return axios({
        url: '/tag/addPicTag',
        method: 'POST',
        params:{
            pid:pid,
            tag:tag
        }
    })
}
export function DeletePicTag(pid,tag){
    return axios({
        url: '/tag/deletePicTag',
        method: 'POST',
        params:{
            pid:pid,
            tag:tag
        }
    })
}
