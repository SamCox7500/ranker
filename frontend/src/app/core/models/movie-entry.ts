import { MediaEntry } from "./media-entry";

export interface MovieEntry extends MediaEntry {
    title: string;
    release_date: string;
    poster_path: string;
}
