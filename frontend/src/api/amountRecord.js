import axios from "@/utils/axios";
export default {

    clientUserAmountRecordList(page, limit, houseName, operator) {
        return axios({
            url: '/clientUserAmountRecordList',
            method: 'get',
            params: { page, limit, houseName, operator }
        })
    },

    deleteAmountRecord(id) {
        return axios({
            url: '/deleteAmountRecord',
            method: 'post',
            data: { id }
        })
    },
}
