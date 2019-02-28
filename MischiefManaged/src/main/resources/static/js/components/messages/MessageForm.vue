<template>
    <v-layout row>
        <v-text-field
                label="Write something..."
                v-model="text"
        />       <!--благодаря v-model все изменения в этом импуте, будут попадать в поле text-->

        <v-btn icon @click="save">
            <v-icon>send</v-icon>
        </v-btn>
    </v-layout>
</template>

<script>
    import messagesApi from 'api/messages'

    export default{
        props:['messages', 'messageAttr'],
        data() {      //data не объект а функция которая возвращает объект
            return {
                text: '',
                id: ''
            }
        },
        watch: {        //эта функция запускается при любои изменениии messageAttr
            messageAttr(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {
            save() {     //функции верхнего уровня должны быть безымянными, что бы быть привязанными к компоненту, к которому они относятся
                const message = {
                    id: this.id,
                    text: this.text
                    }

                if(this.id){
                    messagesApi.update(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id === data.id)        //перед тем как перезаписать сообщение, нужно удалить старое
                            this.messages.splice(index, 1, data)        //(индекс элемента который нужно ззменить, количество элементов на замену, на какой заменить)
                        })
                    )
                }else{
                    messagesApi.add(message).then(result =>     //метод save присутствует во Vue.resource
                          result.json().then(data => {
                                const index = this.messages.findIndex(item => item.id === data.id)

                                if(index > -1) {
                                    this.messages.splice(index, 1, data)
                                }else {
                                    this.messages.push(data)
                                }
                          })

                    )
                }
                this.text = ''
                this.id = ''
            }
        }
    }
</script>

<style>

</style>