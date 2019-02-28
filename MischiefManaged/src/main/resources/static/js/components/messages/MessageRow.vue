<template>
    <v-card class="my-1">
        <v-card-title primary-title>
            <i>({{message.id}})</i>
            {{message.text}}
            <v-spacer></v-spacer>
            <span class="grey--text">{{message.creationDate}}</span>
        </v-card-title>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn icon @click="edit">
                    <v-icon>
                        edit
                    </v-icon>
                </v-btn>
                <v-btn icon @click="del">
                    <v-icon>
                        delete
                    </v-icon>
                </v-btn>
            </v-card-actions>
    </v-card>
</template>

<script>
    export default {
        props:['message', 'editMessage', 'deleteMessage', 'messages'],
        methods:{
            edit() {
                this.editMessage(this.message)
            },
            del() {
               this.deleteMessage(this.message)
            }
        }
    }
</script>

<style>

</style>