import os
import sys
import requests
from csv2md import table
import csv
import math

def split_into_chunks(data, max_rows=300):  # Reduced chunk size
    """Split data into chunks that won't exceed GitHub's size limit."""
    return [data[i:i + max_rows] for i in range(0, len(data), max_rows)]

def estimate_markdown_size(markdown_text):
    """Estimate the size of markdown text in characters."""
    return len(markdown_text)

def create_github_issue(headers, title, body, attempt=1):
    """Create a GitHub issue with error handling and retry logic."""
    try:
        # Check body size before making request
        body_size = estimate_markdown_size(body)
        print(f"Issue body size: {body_size} characters")
        
        if body_size > 65000:  # Leave some margin from the 65536 limit
            print(f"Body size too large ({body_size} characters). Skipping.")
            return False
            
        response = requests.post(
            "https://api.github.com/repos/CSCI5308/course-project-g02/issues",
            headers=headers,
            json={"title": title, "body": body}
        )
        
        print(f"API Response Status Code: {response.status_code}")
        
        if response.status_code == 201:
            return True
        else:
            print(f"API Response Text: {response.text}")
            return False
            
    except Exception as e:
        print(f"Error creating issue: {str(e)}")
        return False

def process_csv_file(file_path, headers, commit):
    """Process a single CSV file and create issues."""
    try:
        print(f"\nProcessing: {file_path}")
        file_size = os.path.getsize(file_path)
        print(f"File size: {file_size} bytes")
        
        with open(file_path) as csv_file:
            all_smells = list(csv.reader(csv_file))
        
        if len(all_smells) <= 1:
            print("File has no data (only headers or empty)")
            return
            
        headers_row = all_smells[0]
        data_rows = all_smells[1:]
        
        print(f"Total rows: {len(all_smells)}")
        print(f"Data rows: {len(data_rows)}")
        
        # Split data into smaller chunks
        data_chunks = split_into_chunks(data_rows)
        total_chunks = len(data_chunks)
        
        print(f"Split into {total_chunks} chunks")
        
        filename = os.path.basename(file_path)
        successful_chunks = 0
        
        for i, current_chunk in enumerate(data_chunks, 1):
            chunk_with_headers = [headers_row] + current_chunk
            
            # Generate markdown table
            try:
                raw_md = table.Table(chunk_with_headers).markdown()
            except Exception as e:
                print(f"Error generating markdown for chunk {i}: {str(e)}")
                continue
            
            # Create issue title
            base_title = f"{filename.replace('.csv', '')} for commit - {commit}"
            title = f"{base_title} (Part {i}/{total_chunks})" if total_chunks > 1 else base_title
            
            # Add summary information
            summary = f"""
### Code Quality Analysis Report

**File**: {filename}
**Total Records**: {len(data_rows)}
**Records in this chunk**: {len(current_chunk)}
**Chunk**: {i} of {total_chunks}

---

"""
            body = summary + raw_md
            
            # Try to create the issue
            if create_github_issue(headers, title, body):
                successful_chunks += 1
                print(f"✓ Created issue {i}/{total_chunks}")
            else:
                print(f"✗ Failed to create issue {i}/{total_chunks}")
        
        print(f"Successfully created {successful_chunks}/{total_chunks} issues for {filename}")
        
    except Exception as e:
        print(f"Error processing file: {str(e)}")
        return False

def main():
    if len(sys.argv) != 3:
        print("Usage: python issue.py <commit_hash> <github_pat>")
        sys.exit(1)
    
    commit = sys.argv[1]
    pat = sys.argv[2]
    
    current_dir = os.getcwd()
    path_to_smells = os.path.join(current_dir, "report")
    
    print(f"Working directory: {current_dir}")
    print(f"Report directory: {path_to_smells}")
    
    if not os.path.exists(path_to_smells):
        print(f"Report directory not found: {path_to_smells}")
        sys.exit(1)
    
    headers = {
        "Authorization": f"Bearer {pat}",
        "Accept": "application/vnd.github+json",
    }
    
    # Get list of CSV files and sort them by size
    smell_files = []
    for f in os.listdir(path_to_smells):
        if f.endswith(".csv"):
            file_path = os.path.join(path_to_smells, f)
            size = os.path.getsize(file_path)
            smell_files.append((file_path, size))
    
    # Sort files by size (process smaller files first)
    smell_files.sort(key=lambda x: x[1])
    
    print(f"\nFound {len(smell_files)} CSV files to process:")
    for file_path, size in smell_files:
        print(f"- {os.path.basename(file_path)}: {size} bytes")
    
    # Process each file
    for file_path, _ in smell_files:
        process_csv_file(file_path, headers, commit)

if __name__ == "__main__":
    main()
