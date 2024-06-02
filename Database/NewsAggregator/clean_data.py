# Define the file paths
file_path = 'news_test.csv'
new_file_path_delim = 'news.csv'

# Open the file and read its content with the new delimiter "::"
with open(file_path, 'r', encoding='utf-8') as file:
    lines = file.readlines()

# Extract the first line (header)
header = lines[0]
data_lines = lines[1:]

# Filter out lines with more than 12 fields
filtered_lines = [line for line in data_lines if len(line.split('::')) <= 12]

# Remove duplicate lines and sort
unique_lines = list(set(filtered_lines))
unique_lines.sort()

# Replace the IDs with sequential numbers
modified_lines = [header]  # Start with the header
for index, line in enumerate(unique_lines):
    fields = line.split('::')
    fields[0] = str(index + 1)
    modified_line = '::'.join(fields)
    modified_lines.append(modified_line)

# Write the modified content back to a new file
with open(new_file_path_delim, 'w', encoding='utf-8') as file:
    file.writelines(modified_lines)

print(f'Modified file saved to: {new_file_path_delim}')

