<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>Mischief Managed</v-toolbar-title>
            <v-btn flat round
                   v-if="profile"
                   :disabled="$route.path === '/'"
                   @click="showMessages"
            >
                Messages
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn flat round
                   v-if="profile"
                   :disabled="$route.path === '/profile'"
                   @click="showProfile"
            >
                <v-avatar class="mx-1"
                        :size=30
                        color="grey lighten-4"
                >
                    <img :src="profile.userpic" alt="avatar">
                </v-avatar>

                <span v-if="profile">{{profile.name}}</span>
            </v-btn>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>

        </v-toolbar>

        <v-content>
            <router-view></router-view>
        </v-content>


    </v-app>
</template>

<script>
import { mapState, mapMutations } from 'vuex'
import { addHandler } from 'util/ws'

    export default {
        computed: mapState(['profile']),                                                                        //Для mapState и mapGetters используем computed раздел для подключения.
        methods: {
            ...mapMutations(['addMessageMutation', 'updateMessageMutation', 'removeMessageMutation']),          //Для mapMutations и mapActions используем methods раздел для подключения
            showMessages(){
                this.$router.push('/')
            },
            showProfile(){
                this.$router.push('/profile')
            }
        },
        created() {
            addHandler(data => {
                if(data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE' :
                            this.addMessageMutation(data.body)
                            break
                        case 'UPDATE' :
                            this.updateMessageMutation(data.body)
                            break
                        case 'REMOVE' :
                            this.removeMessageMutation(data.body)
                            break
                        default:
                            console.error('Looks like the event type is unknown "${data.eventType}"')
                    }
                } else {
                    console.error('Looks like the Object Type type is unknown "${data.objectType}"')
                }
            })

        },
        beforeMount(){                          //до того как все заиницилизируется, будем проверять активирован ли профиль
            if(!this.profile) {
                this.$router.replace('/auth')   //и если он не активирован, то перенаправляем (router.replace работает, как router.push, с той лишь разницей, что переход осуществляется без добавления новой записи в историю навигации, а заменяет текущую запись в нём.)
            }
        }
    }
</script>

<style>

</style>