package me.elephant1214.paperfixes.configuration;

import me.elephant1214.paperfixes.configuration.parts.Bugfixes;
import me.elephant1214.paperfixes.configuration.parts.Features;
import me.elephant1214.paperfixes.configuration.parts.Performance;

@SuppressWarnings("unused")
class ConfigContent {
    public final String guide = "See https://github.com/Elephant1214/PaperFixes/blob/main/DETAILS.md for information about each option.";
    public Bugfixes bugfixes = new Bugfixes();
    public Features features = new Features();
    public Performance performance = new Performance();
}
