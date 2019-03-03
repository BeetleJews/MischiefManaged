import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from 'api/messages'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {      //состояние это единый контейнер для хранения наших объектов
        messages: frontendData.messages,
        profile: frontendData.profile
  },
  getters: {        //геттеры это вычисляемые поля которые будут храниться в store и будут доступны отовсюду(геттеры не модифицируют данные а только возвращают какое то представление данных)
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id))
  },
  mutations: {      //мутации это синхронные события которые могут изменить данные
        addMessageMutation (state, message) {       //message это аргумент который будет приходить снаружи
            state.messages = [          //заменяем на новый масив,
                ...state.messages,      //в который помещаем старое значение
                message                 //и новый message
            ]
        },
        updateMessageMutation (state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id)
            state.messages = [
                ...state.messages.slice(0, updateIndex),       //остановился тут 11:00
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },
        removeMessageMutation (state, message) {
            const deletionIndex = state.messages.findIndex(item => item.id === message.id)

            if(deletionIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, deletionIndex),
                    ...state.messages.slice(deletionIndex + 1)
                ]
            }
        },

  },
  actions: {        //действия это асинхронные события, которые сами никаких модификаций не производят, но позволяют отпустить поток в исполнение далее
        async addMessageAction({commit, state}, message) {     //async нужен для того что бы избежать огромного количества then
            const result = await messagesApi.add(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id)

            if(index > -1) {
                commit('updateMessageMutation', data)
            }else {
                commit('addMessageMutation', data)
            }
        },
        async updateMessageAction({commit}, message) {      //асинхронны из за того что вызывают messagesApi(обращаются к серверу)
            const result = await messagesApi.update(message)
            const data = await result.json()
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit}, message) {
            const result = await messagesApi.remove(message.id)

            if (result.ok) {
                commit('removeMessageMutation', message)
            }
        },

  }
})

