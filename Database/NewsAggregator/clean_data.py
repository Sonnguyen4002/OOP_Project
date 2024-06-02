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

# Identify and remove lines with duplicate values in the second field
second_field_seen = set()
unique_second_field_lines = []
duplicate_second_field_lines = []

for line in filtered_lines:
    fields = line.split('::')
    if len(fields) > 1:
        second_field_value = fields[1]
        if second_field_value in second_field_seen:
            duplicate_second_field_lines.append(line)
        else:
            second_field_seen.add(second_field_value)
            unique_second_field_lines.append(line)

# Print duplicate lines in the second field
print("Duplicate values in the second field:")
for line in duplicate_second_field_lines:
    print(line.strip())

# Remove duplicate lines and sort the remaining lines
unique_lines = list(set(unique_second_field_lines))
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
