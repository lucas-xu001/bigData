import axios from '@/utils/axios'
export default {
    //查询评论
    getCommentList(houseId, landlordId, page, limit) {
        return axios({
            url: '/houseCommentList',
            method: 'get',
            params: { houseId, landlordId, page, limit }
        })
    },
    //新增评论
    saveHouseComment(orderId, houseId, content, score) {
        return axios({
            url: '/saveClientHouseComment',
            method: 'post',
            data: { orderId, houseId, content, score }
        })
    },
}
