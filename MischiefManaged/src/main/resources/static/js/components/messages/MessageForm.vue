<template>
    <v-layout row>
        <v-text-field
                label="Write something..."
                v-model="text"
                @keyup.enter="save"
        />       <!--благодаря v-model все изменения в этом импуте, будут попадать в поле text-->

        <v-btn icon @click="save">
            <v-icon>send</v-icon>
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex'

    export default{
        props:['messageAttr'],
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
            ...mapActions(['addMessageAction', 'updateMessageAction']),
            save() {
                const message = {
                    id: this.id,
                    text: this.text
                }
                if (this.id) {
                    this.updateMessageAction(message)
                } else {
                    this.addMessageAction(message)
                }
                this.text = ''
                this.id = ''
            }
        }
    }
</script>

<style>

</style>