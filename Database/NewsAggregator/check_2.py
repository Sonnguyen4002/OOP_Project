import pandas as pd

# Load the file
file_path = 'news.csv'

# Read the file
data = pd.read_csv(file_path, delimiter='::', engine='python', header=0)

# Check for duplicates
duplicates = data[data.duplicated()]

# Display duplicates
if not duplicates.empty:
    print("Duplicate lines found:")
    print(duplicates)
else:
    print("No duplicate lines found.")
