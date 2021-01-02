import { Category } from "../_models/entity/category";
import { Post } from "../_models/entity/post";
import { UserInfo } from "../_models/entity/user-info";

export const USER_ENTITY: UserInfo = {
    userId: "753",
    username: "user_name",
    firstName:"test name",
    lastName:"test surname",
    role: "USER",
};

export const USER_ADMIN: UserInfo = {
    userId: "2",
    username: "ADMIN-1",
    firstName:"admin test",
    lastName:"surname admin",
    role: "ADMIN",
};

export const POST_ENTITY_LIST: Post[] = [
    { id: 1, name: 'Dragon Burning Cities' },
    { id: 2, name: 'Sky Rains Great White Sharks' },
    { id: 3, name: 'Giant Asteroid Heading For Earth' },
    { id: 4, name: 'Procrastinators Meeting Delayed Again' },
  ];

export const CATEGORY_ENTITY_LIST: Category[] = [
    { id: 1, name: 'Dragon Burning Cities' },
    { id: 2, name: 'Sky Rains Great White Sharks' },
    { id: 3, name: 'Giant Asteroid Heading For Earth' },
    { id: 4, name: 'Procrastinators Meeting Delayed Again' },
  ];