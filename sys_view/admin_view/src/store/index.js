import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    queryData:{}, // 用于存储查询条件,
  },
  getters: {
  },
  mutations: {
    setQueryData(state, data) {
      state.queryData = data
    },
  },
  actions: {
  },
  modules: {
  }
})
