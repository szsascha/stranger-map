<template>
   <v-app id="inspire">
      <v-content>
         <v-container fluid fill-height>
            <v-layout align-center justify-center>
               <v-flex xs12 sm8 md4>
                  <v-card class="elevation-12">
                     <v-toolbar dark color="primary">
                        <v-toolbar-title>Join stranger map</v-toolbar-title>
                     </v-toolbar>
                     <v-card-text>
                        <v-form>
                        <div class="overflow-y-auto">
                        <v-btn-toggle
                        v-model="username"
                        tile
                        color="blue darken-3"
                        group
                        >
                        <v-btn value="😁" style="font-size: 3em">😁</v-btn> 
                        <v-btn value="🥸" style="font-size: 3em">🥸</v-btn> 
                        <v-btn value="😎" style="font-size: 3em">😎</v-btn> 
                        <v-btn value="🤠" style="font-size: 3em">🤠</v-btn> 
                        <v-btn value="🙂" style="font-size: 3em">🙂</v-btn> 
                        </v-btn-toggle></div>
                           <v-textarea
                              id="description"
                              name="description"
                              label="A short description about yourself (max. 500 characters)"
                              type="text"
                              v-model="description"
                           ></v-textarea>
                        </v-form>
                     </v-card-text>
                     <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" @click="join">Join</v-btn>
                     </v-card-actions>
                  </v-card>
               </v-flex>
            </v-layout>
         </v-container>
      </v-content>
   </v-app>
</template>

<script>
import Vue from 'vue'

export default {
   name: 'Join',
   props: {
      source: String,
   },
   data() {
       return {
        username: '',
        description: ''
       }
   },
   methods: {
       join() {
            fetch("http://localhost:8081/api/session", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    name: this.username,
                    description: this.description
                })
            })
            .then(response => { 
                if(response.ok){
                    return response.json();
                } else{
                    alert("Server returned " + response.status + " : " + response.statusText);
                }                
            })
            .then(response => {
                    Vue.prototype.uuid = response.uuid;
                    Vue.prototype.ownname = this.username;
                    this.$router.push('map');
            })
            .catch(err => {
                console.log(err);
            });
        }
       
   }
};
</script>

<style></style>
