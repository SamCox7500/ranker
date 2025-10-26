import { User } from "./user";
import { MediaList } from "./media-list";

export interface NumberedRanking {
    id: number | null;
    //userDTO: User | null;
    title: string;
    description: string;
    isPublic: boolean;
    mediaType: string;
    rankingType: string;
    isReverseOrder: boolean;
    mediaListDTO: MediaList;
}
