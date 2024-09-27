import { User } from "./user";

export interface CreateRankingDTO {
    user: User | null;
    title: string;
    description: string;
    isPublic: boolean;
    isReverseOrder: boolean;
    mediaType: string;
}
