import {
    createRouter,
    createWebHistory
} from 'vue-router'
import LoginComponent from '../components/LoginComponent.vue'
import Registration from '../components/RegistrationComponent.vue'
import Items from '../views/ItemView.vue'

const router = createRouter({
    history: createWebHistory(
        import.meta.env.BASE_URL),
    routes: [{
            path: '/',
            name: 'login',
            component: LoginComponent
        },
        {
            path: '/items',
            name: 'items',
            component: Items
        },
        {
            path: '/admin',
            name: 'admin',
            component: () => import('../views/AdminView.vue'),
        },
        {
            path: '/register',
            name: 'register',
            component: Registration,
        },
    ]
})

export default router;