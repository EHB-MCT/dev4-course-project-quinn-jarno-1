<!-- ADMIN PAGE MET OVERVIEW VAN USERS (OM RECHTEN TE GEVEN EN DERGELIJKE) -->

<script>
    import User from './User.vue';

    export default {
        data() {
            return {
                users: []
            }
        },
        methods: {
            async getData() {
                const res = await fetch("http://localhost:9000/users");
                const finalRes = await res.json();
                this.users = finalRes;
                console.log(this.users);
            }
        },
        mounted() {
            this.getData();
        },
        components: {
            User
        }
    }
</script>

<template>
  <div>
    <h3>
        <ul>
            <User v-bind="user in users" :username="user.username" :picture="user.picture" />
        </ul>
    </h3>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

@media (min-width: 1024px) {
}
</style>
