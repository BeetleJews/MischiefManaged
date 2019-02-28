<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>Mischief Managed</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-avatar v-if="profile" class="mx-1"
                    :size=40
                    color="grey lighten-4"
            >
                <img :src="profile.userpic" alt="avatar">
            </v-avatar>
            <span v-if="profile">{{profile.name}} {{profile.name}}</span>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>

        </v-toolbar>

        <v-content>
            <v-container v-if="!profile">
                Автаризация через <a href="/login">Google</a>
            </v-container>

            <v-container v-if="profile">
                <messages-list :messages ="messages"/>
            </v-container>

        </v-content>


    </v-app>
</template>

<script>
import MessagesList from 'components/messages/MessageList.vue'
import { addHandler } from 'util/ws'

    export default {
        components: {
            MessagesList
        },
        data() {
        return{
            messages: frontendData.messages,
            profile: frontendData.profile
            }
        },
        created() {
            addHandler(data => {
                if(data.objectType === 'MESSAGE') {
                    const index = this.messages.findIndex(item => item.id === data.body.id)
                    switch (data.eventType) {
                        case 'CREATE' :
                        case 'UPDATE' :
                            if(index > -1) {
                                this.messages.splice(index, 1, data.body)
                            } else {
                                this.messages.push(data.body)
                            }
                            break
                        case 'REMOVE' :
                            this.messages.splice(index, 1)
                            break
                        default:
                            console.error('Looks like the event type is unknown "${data.eventType}"')
                    }
                } else {
                    console.error('Looks like the Object Type type is unknown "${data.objectType}"')
                }
            })

        }
    }
</script>

<style>

</style>