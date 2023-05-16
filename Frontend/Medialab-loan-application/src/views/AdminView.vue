<script setup>
    import HelloWorld from '../components/BannerComponent.vue'
    import AdminComponent from '../components/AdminComponent.vue'
</script>

<template>
    <header>
        <img alt="Vue logo" class="logo" src="../assets/ehblogo.jpg" width="225" height="225" />

        <div class="wrapper">
            <HelloWorld title="Admin Dashboard" />
            <HelloWorld msg="Edit & Delete Users, Items And Loans" />
        </div>
    </header>

    <main>
        <AdminComponent />
    </main>
</template>

<style scoped>
header {
    line-height: 1.5;
}

.logo {
    display: block;
    margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
    header {
        display: flex;
        place-items: center;
        padding-right: calc(var(--section-gap) / 2);
    }

    .logo {
        margin: 0 2rem 0 0;
    }

    header .wrapper {
        display: flex;
        place-items: flex-start;
        flex-wrap: wrap;
    }
}
</style>
