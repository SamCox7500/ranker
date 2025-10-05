import { MediaEntry } from "./media-entry";

export interface TVShowEntry extends MediaEntry {
    name: string;
    first_air_date: string;
    last_air_date: string;
    number_of_seasons: string;
    number_of_episodes: string;
    poster_path: string;
}
