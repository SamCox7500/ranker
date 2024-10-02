import { MovieSearchResult } from "./movie-search-result";
import { TVShowSearchResult } from "./tvshow-search-result";

export type MediaSearchResult = TVShowSearchResult | MovieSearchResult;
