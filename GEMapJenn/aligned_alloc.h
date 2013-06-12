/*
This file is part of Jenn.
Copyright 2001-2007 Fritz Obermeyer.

Jenn is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Jenn is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Jenn; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

#ifndef NONSTD_ALIGNED_ALLOC_H
#define NONSTD_ALIGNED_ALLOC_H

namespace nonstd
{

//memory functions
void* alloc_blocks (size_t blockSize,
                    size_t numBlocks);
void  free_blocks (void* base);
void  clear_block (void* base,
                   size_t blockSize);
void copy_blocks (void*  destin_base,
                  const void*  source_base,
                  size_t blockSize,
                  size_t numBlocks);
}

#endif
