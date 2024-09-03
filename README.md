# Sport Retailer Analysis

This project implements a Hadoop MapReduce job to analyze sports retail data. It calculates the total sales per retailer and city from a dataset, providing insights into sales performance across different regions. The results are sorted by total sales in descending order, allowing for quick identification of the highest performing areas.

## Table of Contents

- [Installation](#installation)
- [Data Format](#data-format)
- [Usage](#usage)
- [Building the Project](#building-the-project)
- [Contributing](#contributing)
- [License](#license)

## Installation

To run this project, you need a Hadoop environment. Here's how you can set it up:

### Prerequisites

- Java 8 or later.
- Hadoop 2.x or later.

### Setting up Hadoop

1. Download and install Hadoop from the [official Apache Hadoop website](http://hadoop.apache.org).
2. Configure `core-site.xml`, `hdfs-site.xml`, and `mapred-site.xml` as per your cluster's specification.
3. Make sure Hadoop is in your PATH.

## Data Format

The expected format of the input data is a CSV file with the following columns:

- Retailer Name
- Product Name
- City
- State
- Price
- Units Sold

Ensure your input CSV files are preprocessed to conform to this format and placed in a directory accessible by Hadoop.

## Usage

To run the Sport Retailer Analysis MapReduce job, use the following commands:

```bash
# Navigate to your Hadoop installation directory
cd path/to/hadoop

# Run the MapReduce job
bin/hadoop jar path/to/SportRetailerAnalysis.jar SportRetailerAnalysis <input_path> <output_path>
