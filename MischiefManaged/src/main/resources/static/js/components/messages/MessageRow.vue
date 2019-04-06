<template>
    <v-card class="my-1">
        <v-card-text primary-title>
            <div>

                <v-avatar
                        v-if="message.author && message.author.userpic"
                        size="56px"
                >
                    <img
                            :src="message.author.userpic"
                            :alt="message.author.name"
                    >
                </v-avatar>

                <v-avatar
                        v-else
                        size="36px"
                        color="indigo"
                >
                    <v-icon dark>account_circle</v-icon>
                </v-avatar>
                <span class="pl-3">
                    {{ authorName }}
                </span>
            </div>

            <div class="pt-3">{{message.text}}</div>


        </v-card-text>
        <media v-if="message.link" :message="message"></media>
            <v-card-actions class="mx-3">
                <span class="grey--text">{{message.creationDate}}</span>
                <v-spacer></v-spacer>
                <v-icon>
                    chat_bubble_outline
                </v-icon>
                <span v-if="!message.comments"
                      class="grey--text"
                >
                    0
                </span>
                <span v-else
                      class="grey--text"
                >
                    &nbsp{{message.comments.length}}
                </span>
                <v-spacer></v-spacer>
                <v-spacer></v-spacer>
                <v-spacer></v-spacer>
                <v-spacer></v-spacer>
                <v-spacer></v-spacer>
                <v-spacer></v-spacer>
                <v-spacer></v-spacer>
                <v-btn icon @click="edit">
                    <v-icon>
                        edit
                    </v-icon>
                </v-btn>
                <v-btn
                        icon
                        @click="del">
                    <v-icon>
                        delete
                    </v-icon>
                </v-btn>
            </v-card-actions>


            <comment-list
                :comments="message.comments"
                :message-id="message.id"
            ></comment-list>
    </v-card>
</template>

<script>
    import { mapActions } from 'vuex'
    import Media from 'components/media/Media.vue'
    import CommentList from '../comment/CommentList.vue'

    export default {
        props:['message', 'editMessage'],
        components: { CommentList, Media },
        computed: {
            authorName() {
                return this.message.author ? this.message.author.name : 'unknown'        //обрабатываем ситуацию когда автор не задан
             }
        },
        methods:{
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
               this.removeMessageAction(this.message)
            }
        }
    }
</script>

<style>

</style>