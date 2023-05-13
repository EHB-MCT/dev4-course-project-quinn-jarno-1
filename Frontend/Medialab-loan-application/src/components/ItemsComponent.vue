<!-- OVERVIEW VAN DE ITEMS -->

<script>
    export default {
        data() {
            return {
                items: []
            }
        },
        methods: {
            log() {
                console.log("Working");
            },
            async getData() {
                const res = await fetch("http://localhost:9000/items");
                const finalRes = await res.json();
                this.items = finalRes.map(m => {
                    let time  = new Date(m.time);
                    m.time = `${time.getHours()}:${time.getMinutes()}`
                    return m
                });
                //this.messages = finalRes;
            }
        },
        mounted() {
            this.getData();
        }
    }
</script>

<template>
    <div class="item">
        <div id="itemContainer">
            <div v-bind="item in items" class="itemClass">
                <h1>Test</h1>
                 <div class="header">
                    <h3 class="author">{{item.username}}</h3>
                    <h3 class="time">{{item.time}}</h3>
                </div>
                <p>
                    {{ item.item }}
                </p>
            </div>
      </div>
    </div>
</template>

<style scoped>
    #itemContainer {
        overflow-y: scroll;
        width: 100%;
    }

    .itemClass {
        width: 80%;
        padding: 20px;
        color: #fff;
        box-sizing: border-box;
        background-color: #333;
        border-radius: 5px;
        margin: 10px;
        float: left;
    }
    .own {
        margin-left: 20%;
        float: left;
        background-color: #fefefe;
        border: 1px solid #eee;
        color: #333;
    }
</style>