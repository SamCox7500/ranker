import { MediaListEntry } from "./media-list-entry";
import { Ranking } from "./ranking";

export interface MediaList {
    id: number;
    mediaType: string;
    mediaListEntryDTOList: MediaListEntry[];
    numberedRankingDTO: Ranking;
}
