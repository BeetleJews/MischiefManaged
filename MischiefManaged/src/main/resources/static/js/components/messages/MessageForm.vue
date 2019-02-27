<template>
    <div>
        <input type="text" placeholder="Write something..." v-model="text" />       <!--благодаря v-model все изменения в этом импуте, будут попадать в поле text-->
        <input type="button" value="Save" @click="save" />
    </div>
</template>

<script>
    import { sendMessage } from 'util/ws'

    export default{
        props:['messages', 'messageAttr'],
        data() {      //data не объект а функция которая возвращает объект
            return {
                text: '',
                id: ''
            }
        },
        watch: {        //эта функция запускается при любои изменениии messageAttr
            messageAttr: function(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {
            save() {     //функции верхнего уровня должны быть безымянными, что бы быть привязанными к компоненту, к которому они относятся
                sendMessage({id: this.id, text: this.text})
                this.text = ''
                this.id = ''

                /*const message = { text: this.text }

                if(this.id){
                    this.$resource('/message{/id}').update({id: this.id}, message).then(result =>
                        result.json().then(data => {
                            const index = getIndex(this.messages, data.id)        //перед тем как перезаписать сообщение, нужно удалить старое
                            this.messages.splice(index, 1, data)        //(индекс элемента который нужно ззменить, количество элементов на замену, на какой заменить)
                            this.text = ''
                            this.id = ''
                        })
                    )
                }else{
                    this.$resource('/message{/id}').save({}, message).then(result =>     //метод save присутствует во Vue.resource
                          result.json().then(data => {
                                this.messages.push(data)
                                this.text = ''
                          })

                    )
                }*/
            }
        }
    }
</script>

<style>

</style>