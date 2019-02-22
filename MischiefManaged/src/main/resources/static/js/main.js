
function getIndex(list, id) {        //определяет индекс элемента в колекции
    for(var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

var messageApi = Vue.resource('/message{/id}');

Vue.component('message-form', {
    props:['messages', 'messageAttr'],
    data: function() {      //data не объект а функция которая возвращает объект
        return {
            text: '',
            id: ''
        }
    },
    watch: {        //эта функция запускается при любои изменениии messageAttr
        messageAttr: function(newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
            '<input type="text" placeholder="Write something..." v-model="text" />' +  //благодаря v-model все изменения в этом импуте, будут попадать в поле text
            '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {     //функции верхнего уровня должны быть безымянными, что бы быть привязанными к компоненту, к которому они относятся
            var message = { text: this.text };

            if(this.id){
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.messages, data.id);        //перед тем как перезаписать сообщение, нужно удалить старое
                        this.messages.splice(index, 1, data);        //(индекс элемента который нужно злменить, количество элементов на замену, на какой заменить)
                        this.text = ''
                        this.id = ''
                    })
                )
            }else{
                messageApi.save({}, message).then(result =>     //метод save присутствует во Vue.resource
                      result.json().then(data => {
                            this.messages.push(data);
                            this.text = ''
                      })

                )
            }
        }
    }
});

Vue.component('message-row', {
    props:['message', 'editMethod', 'messages'],
    template:'<div>' +
                '<i>({{message.id}})</i> {{message.text}}' + ' {{message.creationDate}}' +
                '<span style="position: absolute; right: 0">' +
                    '<input type="button" value="Edit" @click="edit" />' +
                    '<input type="button" value="X" @click="del" />' +
                '</span>' +
             '</div>',
    methods:{
        edit: function() {
            this.editMethod(this.message);
        },
        del: function() {
            messageApi.remove({id: this.message.id}).then(result => {
                if (result.ok) {
                    this.messages.splice(this.messages.indexOf(this.message), 1)
                }
            })
        }
    }
});

// Определяем новый компонент под именем messages-list
Vue.component('messages-list', {
    props: ['messages'],
    data: function() {
        return {
            message: null
        }
    },
    template:
              '<div style="position: relative; width: 500px;">' +
                '<message-form :messages="messages" :messageAttr="message" />' +
                '<message-row v-for="message in messages" :key="message.id" :message="message" ' +
                ':editMethod="editMethod" :messages="messages" />' +
              '</div>',
    methods: {
        editMethod: function(message) {
            this.message = message;
        }
    }
});

var app = new Vue({
  el: '#app',
  template:
  '<div>' +
    '<div v-if="!profile" >Автаризация через <a href="/login">Google</a></div>' +
    '<div v-else>' +
        '<div>{{profile.name}}&nbsp;<a href="/logout">Выйти</a></div>' +
        '<messages-list :messages ="messages"/>' +
    '</div>' +
  '</div>',
  data: {
    messages: frontendData.messages,
    profile: frontendData.profile
  },
      created: function() {
//                   messageApi.get().then(result =>
//                       result.json().then(data =>
//                           data.forEach(message => this.messages.push(message))
//                       )
//                   )
      }
});