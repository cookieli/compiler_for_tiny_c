.file "tests/codeGenOfMe/while_03.dcf"
.text
.section .rodata
.LC0:
.string "a < b"
.LC1:
.string "a >= b"
.LC2:
.string "e is true\n"
.LC3:
.string "e is false\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq $1,-33(%rbp)
movq $3,-25(%rbp)
movq $2,-17(%rbp)
movq $7,-9(%rbp)
movb $1,-1(%rbp)
.L1:
movq -9(%rbp),%rax
movq -17(%rbp),%r10
cmpq %r10,%rax
jle .L2
movq -33(%rbp),%rax
movq -25(%rbp),%r10
cmpq %r10,%rax
jge .L2
.L3:
movq -33(%rbp),%rax
movq -25(%rbp),%r10
cmpq %r10,%rax
jge .L4
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L5
.L4:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L5:
movb -1(%rbp),%al
cmpb $0,%al
jle .L6
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movb $0,-1(%rbp)
jmp .L7
.L6:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movb $1,-1(%rbp)
.L7:
movq -33(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-33(%rbp)
movq -17(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-17(%rbp)
jmp .L1
.L2:
movb -1(%rbp),%al
cmpb $0,%al
jle .L8
jmp .L3
.L8:
leave
ret

